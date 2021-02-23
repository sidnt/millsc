println("aesEncrypt.sc")

import $ivy.`com.lihaoyi::os-lib:0.7.2`
import $file.textUtils
import $file.cipherParams
import textUtils.textUtils._
import cipherParams.cipherParams._

val wd = os.pwd
val inputF = wd / "aesDemo" / "plaintext.txt"
val outputF = wd / "aesDemo" / "encrypted.txt"
val inputPlaintext: String = os.read(inputF)

/** a passphrase based AES encrypter script */

import java.security.spec.KeySpec
import java.security.{Key, SecureRandom}
import java.util.Base64

import javax.crypto.{Cipher, SecretKey, SecretKeyFactory}
import javax.crypto.spec.{GCMParameterSpec, PBEKeySpec, SecretKeySpec}


val srng = new SecureRandom()
val salt:Array[Byte] = Array.fill(SaltSizeBytes)(0)
//Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0) //16bytes = 16*8 = 128bits
def allocateSalt(): Unit = srng.nextBytes(salt)
allocateSalt() //allocating random bytes in salt // imperative! // will be used in key generation

print("enter passphrase> ")
val pp = scala.io.StdIn.readLine()

val skf: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
val ks: KeySpec = new PBEKeySpec(pp.toCharArray, salt, PbkdIters, AesKeySizeBytes*8)
val sk: SecretKey = skf.generateSecret(ks)
val k: Key = new SecretKeySpec(sk.getEncoded, "AES")
// for two same passphrase, the key generated will be different because of the random salt

val iv: Array[Byte] = Array.fill(GCMIvSizeBytes)(0)
def allocateIv(): Unit = srng.nextBytes(iv)
allocateIv()

val c = Cipher.getInstance(CipherAlgo)
val gcmPs = new GCMParameterSpec(GCMTagSizeBytes*8, iv)
c.init(Cipher.ENCRYPT_MODE, k, gcmPs)
println("encrypting text in input.txt")
val ecText:Array[Byte] = c.doFinal(inputPlaintext.getBytes)

val toWrite = salt ++ iv ++ ecText
val toWriteB64 = arb2b64(toWrite)

os.write.over(outputF, toWriteB64)
println("written encrypted text to encrypted.txt")
