package libraries.in_toto_utils

/**
  * override the other ver
  */
void call(String cmd){
    def args = ["in-toto-run", " --verbose", 
    " --step-name ${step} --products ${get_products()}",
    " --key bob -- ${cmd}"]

    this.steps.sh( args.join() )
}