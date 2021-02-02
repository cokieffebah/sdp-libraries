package libraries.in_toto_utils

@CleanUp
void call(){
    println "pipelineConfig.intotoCollector: ${pipelineConfig.intotoCollector}"
    
    pipelineConfig.intotoCollector.each{ c ->
        println "for ${c}: pipelineConfig.libraries[${c.library}].in_toto[${c.step}]: ${pipelineConfig.libraries[c.library].in_toto[c.step]}"
    }
}

List get_steps(){

}

void create_layout(Map args){

}