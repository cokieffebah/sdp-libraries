package libraries.in_toto_utils

@BeforeStep()
void call(){
    String queue_name = config.step_queue_name ?: "intotoCollector"
    pipeline_config[queue_name] << [step: hookContext.step, library: hookContext.library]
}