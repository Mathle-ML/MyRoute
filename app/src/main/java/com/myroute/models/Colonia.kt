package com.myroute.models

class Colonia(val idColn: String,val refRoutsArray: Array<String>, val colnColinArray: Array<String>, val colnRadio: Double) {
    fun getIDColn() : String{
        return idColn
    }
    fun getRefRouts() : Array<String>{
        return refRoutsArray
    }
    fun getColnColin() : Array<String>{
        return colnColinArray
    }
    fun getRadio() : Double{
        return colnRadio
    }
}