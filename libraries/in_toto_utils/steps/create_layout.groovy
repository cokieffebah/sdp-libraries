package libraries.in_toto_utils

@CleanUp
void call(){
  List collector = get_collector()
  println "pipelineConfig.intotoCollector: ${collector}"

  Map layout_json = [_type:"layout"]
  layout_json.steps = []
  List steps = layout_json.steps

  // using for because I wanted 'continue'
  for( c in collector ){
      if( get_collector.can_collect(c.library, c.step) ){
        steps << get_collector.layout_config(c.library, c.step)
        steps[steps.size() - 1].name = c.step
        //println "for ${c}: ${get_collector.layout_config(c.library, c.step)}"
      } else {
        println "for ${c}: null"
      }
  }

  node{
    unstash 'workspace'
    writeJSON( json: layout_json, file: "layout.json", pretty:4)
    writeFile( file:"create_layout.py", text: resource("create_layout.py"))
    stash 'workspace'
  }
}

void create_layout(Map args){

}