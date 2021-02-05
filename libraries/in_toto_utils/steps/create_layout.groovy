package libraries.in_toto_utils

@CleanUp
void call(){
  List collector = get_collector()
    println "pipelineConfig.intotoCollector: ${collector}"

    // using for because I wanted 'continue'
    for( c in collector ){
        if( get_collector.can_collect(c.library, c.step) ){
          println "for ${c}: ${get_collector.step_config(c.library, c.step)}"
        } else {
          println "for ${c}: null"
        }
    }
}

void create_layout(Map args){

}