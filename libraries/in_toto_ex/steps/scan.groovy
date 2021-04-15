package libraries.in_toto_ex.steps

void call(){
   //println "printing layout for scan:"
   //println get_intoto_layout('scan')
   String scan_file = config.scan_log ?: 'scan.log'
   sh("ls -ltra . > ${scan_file}")
}