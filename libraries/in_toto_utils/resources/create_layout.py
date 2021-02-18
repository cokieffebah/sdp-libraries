from securesystemslib import interface
from in_toto.models.layout import Layout
from in_toto.models.metadata import Metablock
import json
import argparse

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument("-f", "--funcpath", type=str, default=None, 
    help="the path to functionary key")

  parser.add_argument("-o", "--output", type=str, default="the.layout", 
    help="the name for the output file") 

  parser.add_argument("-j", "--inputjson", type=str, default="layout.json", 
    help="the name for the input json file")      

  parser.add_argument("signerpath", type=str,
   help="the path to the signer key")    

  args = parser.parse_args()    
  signer_path = args.signerpath
  func_path = signer_path + ".pub"

  if args.funcpath:
    func_path = args.funcpath

  with open(args.inputjson) as f:
    read_data = f.read()
    print('in-toto.json: ' + args.inputjson)
    config_json = json.loads(read_data)
    create_layout(config_json, signer_path, func_path, args.output)
    print('create_layout.main')


def create_layout(config_json, signer_path, func_path, layout_file): 
  config_json["keys"] = {} 

  func_key_data = interface.import_rsa_publickey_from_file(func_path)
  config_json["keys"][func_key_data["keyid"]]= func_key_data

  # set the pubKey of each step to the functionary
  for step in config_json["steps"]:
    step["pubkeys"] = [func_key_data["keyid"]]

  layout = Layout.read(config_json)  
  metadata = Metablock(signed=layout)

  signer_key = interface.import_rsa_privatekey_from_file(signer_path)
  metadata.sign(signer_key)
  metadata.dump(layout_file)
  print('created: ' + layout_file)


if __name__ == '__main__':
  main()
