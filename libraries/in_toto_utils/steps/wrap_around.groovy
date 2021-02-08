package libraries.in_toto_utils

void call(String step, body = {}){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside {
        unstash workspace
        record_start( step )
        body()
        record_stop( step )
        stash workspace
    }
}


void record_start(String step){
    def stepWrapper = getBinding().getStep(step)
    Map args = get_collector.step_config( stepWrapper.library, step)
    List cmd = ["in-toto-record start --verbose"]
    cmd << "--step-name ${step}"
    if( args.materials ){
      cmd << expectedListToString("--materials", args.materials)
    }
    cmd << "--key ${args.key}"
    sh( script: cmd.join(" ") )
}

void record_stop(String step){
    def stepWrapper = getBinding().getStep(step)
    Map args = get_collector.step_config( stepWrapper.library, step)
    List cmd = ["in-toto-record stop --verbose"]
    cmd << "--step-name ${step}"
    if( args.products ){
      cmd << expectedListToString("--products", args.products)
    }
    cmd << "--key ${args.key}"
    sh( script: cmd.join(" ") )
}

String expectedListToString(String cmdArg, List expects){
    List output = []
    expects.each{ ex ->
        def current = ex
        if( ex instanceof List ){
            current = []
            ex.each{ ex1 ->
                current << ex1
            }

            current = current.join(" ")
        }
        output << "'${current}'".toString()
    }

    return cmdArg + " " output.join(" " + cmdArg + " ")
}