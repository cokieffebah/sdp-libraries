package libraries.in_toto_utils

@CleanUp
void call(){
  List collector = intoto_utils.get_collector()
  println "pipelineConfig.intotoCollector: ${collector}"
  String signer_path = config.layout.signer_path
  String input_json = config.layout.input_json ?: "layout.json"
  String layout_file = config.layout.output_file ?: "the.layout"

  Map layout_json = [_type:"layout"]
  layout_json.key_paths = [config.functionary.path + ".pub"]
  layout_json.inspect = intoto_utils.inspect_config()
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

  intoto_utils.intoto_wrap{
    dir("final_product"){
      // copy metadata
      sh("cp ../${signer_path} ../${config.functionary.path} ../*.pub ../*.*.link .")
      
      // copy product
      sh("cp ../demo-project.tar.gz .")
      writeJSON( json: layout_json, file: input_json, pretty:4)
      writeFile( file:"create_layout.py", text: resource("create_layout.py"))
      sh("python create_layout.py --output ${layout_file} ${signer_path}")
      sh("rm create_layout.py ${input_json}")
      sh("in-toto-verify --verbose --layout ${layout_file} --layout-key ${signer_path}.pub")
    }
  }

}

void create_final_product(){

}

