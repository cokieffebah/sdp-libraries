package libraries.in_toto_utils

List call(){
    String queue_name = config.collector?.queue_name ?: "intotoCollector"

    if( null == pipelineConfig[queue_name] ){
        pipelineConfig[queue_name] = []
    }
    
    return pipelineConfig[queue_name]
}

boolean can_collect( String lib, String step){
    def library = pipelineConfig.libraries[lib]
    return library && library.in_toto && library.in_toto.containsKey(step)
}

Map step_config( String lib, String step ){
    def ret = null
    def library = pipelineConfig.libraries[lib]
    if( can_collect(lib,step) ){
        ret = library.in_toto[step]
    } 

    return ret
}

Map record_config( String lib, String step ){
    return step_config( lib, step )?.record 
}

Map layout_config( String lib, String step ){
    return step_config( lib, step )?.layout 
}