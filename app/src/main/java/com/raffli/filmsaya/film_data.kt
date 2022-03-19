package com.raffli.filmsaya

class film_data {
    var name: String? = null
    var year: String? = null
    var director: String? = null
    var rating: String? = null
    var key: String? = null
    constructor() {}
    constructor(name: String?, year: String?, director: String?, rating: String?) {
        this.name = name
        this.year = year
        this.director = director
        this.rating = rating
    }
}