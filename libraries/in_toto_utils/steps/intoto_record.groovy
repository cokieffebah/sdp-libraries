package libraries.in_toto_utils

import org.boozallen.plugins.jte.init.primitives.TemplatePrimitiveCollector

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

void run(String step, String command = null ){
    TemplatePrimitiveCollector primitiveCollector = TemplatePrimitiveCollector.current()
    if( primitiveCollector.hasStep(step) ){
        def stepWrapper = primitiveCollector.getStep(step)[0]
        Map args = intoto_utils.record_config( stepWrapper.library, step)
        List cmd = ["in-toto-run --verbose"]
        cmd << "--step-name ${step}"
        
        if( args?.materials ){
        cmd << "--materials ${args.materials}"
        }

        if( args?.products ){
        cmd << "--products ${args.products}"
        }

        if( args?.metadata_dir ){
            cmd << "-d ${args.metadata_dir}"
        }

        cmd << "--key ${config.functionary.path}"
        command = command ?: args?.command
        
        cmd << "-- ${command}"
        sh( script: cmd.join(" ") )
    } else {
        println("in-toto run: no collector step for ${step}")
    }
}

void record_start(String step){
    TemplatePrimitiveCollector primitiveCollector = TemplatePrimitiveCollector.current()
    if( primitiveCollector.hasStep(step) ){
        def stepWrapper = primitiveCollector.getStep(step)[0]
        println("record_start: found step for ${step}: with stepWrapper.library: ${stepWrapper.library}; stepWrapper: ${stepWrapper.inspect()}")
        Map args = intoto_utils.record_config( stepWrapper.getLibrary(), step)
        List cmd = ["in-toto-record start --verbose"]
        cmd << "--step-name ${step}"

        if( args?.materials ){
        cmd << "--materials ${args.materials}"
        }

        if( args?.metadata_dir ){
            cmd << "-d ${args.metadata_dir}"
        }

        cmd << "--key ${config.functionary.path}"
        sh( script: cmd.join(" ") )
    } else {
        println("record_start: no collector step for ${step}")
    }
}

void record_stop(String step){
    TemplatePrimitiveCollector primitiveCollector = TemplatePrimitiveCollector.current()
    if( primitiveCollector.hasStep(step) ){
        def stepWrapper = primitiveCollector.getStep(step)[0]
    
        Map args = intoto_utils.record_config( stepWrapper.library, step)
        List cmd = ["in-toto-record stop --verbose"]
        cmd << "--step-name ${step}"

        if( args?.products ){
        cmd << "--products ${args.products}"
        }
        
        if( args?.metadata_dir ){
            cmd << "-d ${args.metadata_dir}"
        }
        
        cmd << "--key ${config.functionary.path}"
        sh( script: cmd.join(" ") )
    } else {
        println("record_stop: no binding step for ${step}")
    }
}