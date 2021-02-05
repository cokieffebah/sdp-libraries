package libraries.in_toto_utils

void call(String step, body = {}){

    docker.image(config.inside_image).inside {
        unstash 'workspace'
        record_start( step )
        body()
        record_stop( [step: step, key: 'bob'], config)
        stash 'workspace'
    }
}


void record_start(step){
    def stepWrapper = getBinding().getStep(step)
    Map args = get_collector.step_config( stepWrapper.library, step)
    List cmd = ["in-toto-record start --verbose"]
    cmd << " --step-name ${step}"
    if( args.materials ){
      cmd << " --materials ${args.materials}"
    }
    cmd << " --key ${args.key}"
    sh( script: cmd.join("") )
}

void record_stop(Map args, Map config){

}