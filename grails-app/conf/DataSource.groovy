environments {
    development {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "patents"
            }
        }
    }
    test {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "patents"
            }
        }
    }
    production {
        grails {
            mongo {
                host = "localhost"
                port = "27017"
                databaseName = "patents"
            }
        }
    }
}