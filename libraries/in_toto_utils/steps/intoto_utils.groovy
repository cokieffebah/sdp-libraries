package libraries.in_toto_utils

List call(){
    return get_collector()
}

List get_collector(){
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

Map inspect_config( String lib, String step ){
    return config.layout.inspect ?: [:] 
}

void intoto_wrap(body){
    String workspace = config.workspace ?: 'workspace'
    docker.image(config.inside_image).inside {
        unstash workspace
        body()
        stash workspace
    }
}

void generate_functionary_keys(){
    String functionary_path = config.functionary.path
    if( config.functionary.generate )
        sh("ls ${functionary_path} || in-toto-keygen -t rsa -b 2048 ${functionary_path}")
}