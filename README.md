# millsc

### aesDemo
- git clone
- cd to working dir

encryption
- enter plaintext in plaintext.txt
- `amm --watch aesDemo/aesEncrypt.sc`
- enter passphrase to encrypt with
- ciphertext would be overwritten in encrypted.txt

decryption
- `amm --watch aesDemo/aesDecrypt.sc`
- enter the same passphrase
- decrypted text would be overwritten in decrypted.txt


### tgModelsParser
- take html source for any tg data model (from <tbody>...</tbody>)
- `amm --watch tgModelsParser/parseDataModel.sc`
- fields and types along with comments get written to output.txt