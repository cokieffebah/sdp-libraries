package libraries.in_toto_utils

void call(){
    println "pipelineConfig.intotoCollector: ${pipelineConfig.intotoCollector}"
    
    pipelineConfig.intotoCollector.each{ c ->
        if( pipelineConfig.libraries[c.library].in_toto ){
          println "for ${c}: pipelineConfig.libraries[${c.library}].in_toto[${c.step}]: ${pipelineConfig.libraries[c.library].in_toto[c.step]}"
        } else {
          println "for ${c}: null == pipelineConfig.libraries[${c.library}].in_toto"
        }
    }
}

List get_steps(){

}

void create_layout(Map args){

}