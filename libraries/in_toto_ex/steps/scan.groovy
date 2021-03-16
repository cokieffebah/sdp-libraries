package libraries.in_toto_ex.steps

void call(){
   //println "printing layout for scan:"
   //println get_intoto_layout('scan')
   sh("ls -ltra . > scan.log")
}