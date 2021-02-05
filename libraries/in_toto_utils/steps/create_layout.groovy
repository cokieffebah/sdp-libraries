package libraries.in_toto_utils

@CleanUp
void call(){
    println "pipelineConfig.intotoCollector: ${pipelineConfig.intotoCollector}"
    List skip_libs = get_skip_libs()
    List skip_steps = get_skip_steps()
    
    // using for because I wanted 'continue'
    for( c in pipelineConfig.intotoCollector ){
        if( c.library in skip_libs){
          println "skipping: ${c}: skipping library: pipelineConfig.libraries[${c.library}]"
          continue
        }

        if( c.step in skip_steps){
          println "skipping: ${c}: skipping step name"
          continue
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

List get_skip_steps(){
  List steps = config?.create_layout?.skip_steps ?: []
  
  return steps
}

List get_steps(){

}

void create_layout(Map args){

}