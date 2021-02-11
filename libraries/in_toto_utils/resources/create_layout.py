from securesystemslib import interface
from in_toto.models.layout import Layout
from in_toto.models.metadata import Metablock
import json
import argparse

def main():
  parser = argparse.ArgumentParser()
  parser.add_argument("-f", "--funcpath", type=str, default=None, 
      help="the path to functionary key")

  parser.add_argument("signerpath", type=str,
   help="the path to the signer key")    

  args = parser.parse_args()    
  signer_path = args.signerpath
  func_path = signer_path + ".pub"

  if args.funcpath:
    func_path = args.funcpath

  with open('layout.json') as f:
    read_data = f.read()
    print('in-toto.json: ' + read_data)
    config_json = json.loads(read_data)
    create_layout(config_json, signer_path, func_path)
    print('create_layout.main')


def create_layout(config_json, signer_path, func_path): 
  config_json["keys"] = {} 
  for key_path in config_json["key_paths"]:
    key_data = interface.import_rsa_publickey_from_file(key_path)
    config_json["keys"][key_data["keyid"]]= key_data

  layout = Layout.read(config_json)  
  metadata = Metablock(signed=layout)

  signer_key = interface.import_rsa_privatekey_from_file(signer_path)
  metadata.sign(signer_key)
  metadata.dump("the.layout")
  print('created the.layout')


if __name__ == '__main__':
  main()
