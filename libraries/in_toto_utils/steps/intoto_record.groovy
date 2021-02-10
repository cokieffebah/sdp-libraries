package libraries.in_toto_utils

void call(String step, body = {}){
    get_collector.intoto_wrap {
        get_collector.generate_functionary_keys()
        record_start( step )
        body()
        record_stop( step )
    }
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