object cipherParams {
  val CipherAlgo      = "AES/GCM/NoPadding" //in GCM don't reuse an initialisation vector
  val PbkdIters       = 20000
  val AesKeySizeBytes = 32  // 32 * 8 = 256 bits
  val GCMIvSizeBytes  = 16  // 16 * 8 = 128 bits
  val GCMTagSizeBytes = 16
  val SaltSizeBytes   = 16
}