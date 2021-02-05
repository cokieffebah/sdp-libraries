package libraries.in_toto_utils

@CleanUp
void call(){
  List collector = get_collector()
    println "pipelineConfig.intotoCollector: ${collector}"

    // using for because I wanted 'continue'
    for( c in collector ){
        if( pipelineConfig.libraries[c.library].in_toto ){
          println "for ${c}: pipelineConfig.libraries[${c.library}].in_toto[${c.step}]: ${pipelineConfig.libraries[c.library].in_toto[c.step]}"
        } else {
          println "for ${c}: null == pipelineConfig.libraries[${c.library}].in_toto"
        }
    }
}

void create_layout(Map args){

}