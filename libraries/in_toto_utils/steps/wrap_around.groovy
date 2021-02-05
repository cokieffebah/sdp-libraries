package libraries.in_toto_utils

void call(String step, body = {}){

    docker.image(config.inside_image).inside {
        unstash 'workspace'
        record_start( [step: step, key: 'bob'], config)
        body()
        record_stop( stop_args, config)
        stash 'workspace'
    }
}


void record_start(Map args, Map config){
    List cmd = ["in-toto-record start --verbose"]
    cmd << " --step-name ${args.step}"
    if( args.materials ){
      cmd << " --materials ${args.materials}"
    }
    cmd << " --key ${args.key}"
    sh( script: cmd.join("") )
}

void record_stop(Map args, Map config){

}