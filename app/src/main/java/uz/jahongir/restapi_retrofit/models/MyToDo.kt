package uz.jahongir.restapi_retrofit.models

class MyToDo{
    var id: Int = 0
    var holat: String = ""
    var matn: String = ""
    var oxirgi_muddat: String = ""
    var sarlavha: String = ""

    constructor(id: Int, holat: String, matn: String, oxirgi_muddat: String, sarlavha: String) {
        this.id = id
        this.holat = holat
        this.matn = matn
        this.oxirgi_muddat = oxirgi_muddat
        this.sarlavha = sarlavha
    }

    constructor(holat: String, matn: String, oxirgi_muddat: String, sarlavha: String) {
        this.holat = holat
        this.matn = matn
        this.oxirgi_muddat = oxirgi_muddat
        this.sarlavha = sarlavha
    }

}