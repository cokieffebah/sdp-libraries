package libraries.in_toto_utils

void call(String step, body = {}){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside {
        unstash workspace
        generate_functionary_keys()
        record_start( step )
        body()
        record_stop( step )
        stash workspace
    }
}

void generate_functionary_keys(){
    String functionary_path = config.functionary.path
    if( config.functionary.generate )
        sh("ls ${functionary_path} || in-toto-keygen -t rsa -b 2048 ${functionary_path}")
}

void record_start(String step){
    def stepWrapper = getBinding().getStep(step)
    Map args = get_collector.record_config( stepWrapper.library, step)
    List cmd = ["in-toto-record start --verbose"]
    cmd << "--step-name ${step}"
    if( args.materials ){
      cmd << "--materials ${args.materials}"
    }

    if( args.metadata_dir ){
        cmd << "-d ${args.metadata_dir}"
    }

    cmd << "--key ${config.functionary.path}"
    sh( script: cmd.join(" ") )
}

void record_stop(String step){
    def stepWrapper = getBinding().getStep(step)
    Map args = get_collector.record_config( stepWrapper.library, step)
    List cmd = ["in-toto-record stop --verbose"]
    cmd << "--step-name ${step}"
    if( args.products ){
      cmd << "--products ${args.products}"
    }
    
    if( args.metadata_dir ){
        cmd << "-d ${args.metadata_dir}"
    }
    
    cmd << "--key ${config.functionary.path}"
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
        output << "${current}".toString()
    }

    return cmdArg + " " + output.join(" ")
}