package libraries.in_toto_utils

@CleanUp
void call(){
  List collector = intoto_utils.get_collector()
  println "pipelineConfig.intotoCollector: ${collector}"
  String functionary_path = config.functionary.path

  Map layout_json = [_type:"layout"]
  layout_json.steps = []
  List stepList = layout_json.steps

  // using for because I wanted 'continue'
  for( c in collector ){
      if( intoto_utils.can_collect(c.library, c.step) ){
        def layout_config = intoto_utils.layout_config(c.library, c.step)
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
    writeJSON( json: layout_json, file: "layout.json", pretty:4)
    writeFile( file:"create_layout.py", text: resource("create_layout.py"))
    sh("python create_layout -f ${functionary_path}.pub -s ${functionary_path}")
  }

}

