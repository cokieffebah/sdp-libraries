package libraries.in_toto_utils

void call(String step, body = {}){
    std(step, body)
}

void wrapped(String step, body = {}){
    intoto_utils.intoto_wrap {
        intoto_utils.generate_functionary_keys()
        std(step, body)
    }
}

void std(String step, body = {}){
    record_start( step )
    body()
    record_stop( step )
}

void record_start(String step){
    def stepWrapper = getBinding().getStep(step)
    Map args = intoto_utils.record_config( stepWrapper.library, step)
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
    Map args = intoto_utils.record_config( stepWrapper.library, step)
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