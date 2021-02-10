package libraries.in_toto_utils

@BeforeStep( { hookContext.library != "in_toto_utils" })
void call(){
    if( intoto_utils.can_collect( hookContext.library, hookContext.step) )
        intoto_utils.get_collector() << [step: hookContext.step, library: hookContext.library]
}