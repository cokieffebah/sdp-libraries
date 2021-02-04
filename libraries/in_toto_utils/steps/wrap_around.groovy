package libraries.in_toto_utils

void call(body){
//    docker.image(config.inside_image).inside {
//        record_start( [:], config)
//        body()
//        record_stop( [:], config)
//    }
   x = 5
   node{
   body()
   }
}


void record_start(Map args, Map config){

}

void record_stop(Map args, Map config){

}