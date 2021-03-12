package libraries.in_toto_utils

@CleanUp({ config.auto_verify != null && config.auto_verify != false })
void call(){
  create_verify_layout()
}

void create_verify_layout(String layout_file = null, 
  String final_product_dir = null, Boolean show_tamper = null, List inspect_config = null){
  
  layout_file = layout_file ?: config.layout.output_file ?: "the.layout"
  final_product_dir = final_product_dir ?: "final_product"
  String input_json = config.layout.input_json ?: "layout.json"
  String signer_path = config.layout.signer_path

  from_collected_steps(signer_path, layout_file, input_json, true, inspect_config)

  verify_layout("${signer_path}.pub", layout_file, final_product_dir){ 
      // copy in-toto metadata
      sh("cp ../${layout_file} ../${signer_path} ../${config.functionary.path} ../*.pub ../*.*.link .")
      // copy the original intoto demo product tar
      sh("cp ../demo-project.tar.gz .")
  }

  if( show_tamper ){
    println ""
    println "tampering with scan.log and running in-toto-verify"
    verify_layout("${signer_path}.pub", layout_file, final_product_dir){
        // tamper with scan.log
        sh("echo 'extra line' > scan.log")

        // failed on already untarred demo-project/vcs.log ?
        sh("rm -rf demo-project")
    }
  }
}

void from_collected_steps(String signer_path = null, 
  String layout_file = null, String input_json = null,
  boolean archive_output = false, List inspect_config = null, def run_closure = null){

  List collector = intoto_utils.get_collector()
  println "pipelineConfig.intotoCollector: ${collector}"

  signer_path = signer_path ?: config.layout.signer_path
  input_json = input_json ?: config.layout.input_json ?: "layout.json"
  layout_file = layout_file ?: config.layout.output_file ?: "the.layout"

  Map layout_json = [_type:"layout"]
  layout_json.key_paths = [config.functionary.path + ".pub"]
  layout_json.inspect = inspect_config ?: intoto_utils.inspect_config()
  layout_json.steps = []
  List stepList = layout_json.steps

  collector.each { c ->
      if( intoto_utils.can_collect(c.library, c.step) ){
        def layout_config = intoto_utils.step_layout_config(c.library, c.step)
        println "for ${c}: ${layout_config}"
        if( layout_config ){
          stepList << layout_config
          stepList.last().name = c.step

          if( layout_config.threshold ){}
          else{ layout_config.threshold = 1 }
        }
       
      } else {
        println "for ${c}: null"
      }
  }

  def block_run = {
    writeJSON( json: layout_json, file: input_json, pretty:3)
    writeFile( file:"create_layout.py", text: resource("create_layout.py"))
    sh("python create_layout.py --output ${layout_file} ${signer_path}")
    if( archive_output ){
      archiveArtifacts(artifacts: "${input_json}, ${layout_file}")
    }
    sh("rm create_layout.py ${input_json}")
  }

  run_closure ? run_closure(block_run): intoto_utils.intoto_wrap(block_run)
}

void verify_layout(String layout_key_path = null, String layout_file = null, String final_dir = null, body){
  
  layout_key_path = layout_key_path ?: config.verify?.layout_key_path
  layout_file = layout_file ?: config.verify?.output_file
  final_dir = final_dir ?: config.verify?.final_dir

  intoto_utils.intoto_wrap{
    dir(final_dir){
      body()
      def status = sh(returnStatus: true, script: "in-toto-verify --verbose --layout ${layout_file} --layout-key ${layout_key_path}")
      println ""
      println "completed in-toto-verify.status: ${status}"
    }
  }

}

