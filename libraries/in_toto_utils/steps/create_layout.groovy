package libraries.in_toto_utils

@CleanUp
void call(){
    println "pipelineConfig.intotoCollector: ${pipelineConfig.intotoCollector}"
    List skip_libs = get_skip_libs()
    
    pipelineConfig.intotoCollector.each{ c ->
        if( c.library in skip_libs){
          println "skipping: ${c}: pipelineConfig.libraries[${c.library}"
          break
        }

        if( pipelineConfig.libraries[c.library].in_toto ){
          println "for ${c}: pipelineConfig.libraries[${c.library}].in_toto[${c.step}]: ${pipelineConfig.libraries[c.library].in_toto[c.step]}"
        } else {
          println "for ${c}: null == pipelineConfig.libraries[${c.library}].in_toto"
        }
    }
}

List get_skip_libs(){
  List libs = config?.create_layout?.extra_skip_libs ?: []
  libs << "in_toto_utils"

  return libs
}

List get_steps(){

}

void create_layout(Map args){

}