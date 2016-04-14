declare var angular;

class TsController {

    public static $inject = [
        '$scope',
        '$http'
    ];
    $scope;
    $http;

    decl106:string = `{
        "tagName": "declaratie106",
        "xmlns": "mfp:anaf:dgti:d106:declaratie:v1",
        "d_rec": "1",
        "an": "2015",
        "luna": "12",
        "totalPlata_A":"1234",
        "cif": "34983363",
        "den":"str1234",
        "adresa":"str1234",
        "telefon":"str1234",
        "fax":"str1234",
        "email": "alex@example.com",
        "nume_declar":"str1234",
        "prenume_declar":"str1234",
        "functie_declar": "str1234",
        "childNodes": [{
            "tagName": "dividende",
            "denAct":"str1234",
            "cifAct":"34983363",
            "cotaAct":"3.14",
            "divd":"1234"
        }]
    }`;
    response:string;
    pdf:string;

    constructor($scope, $http) {
        $scope.vm = this;
        this.$scope = $scope;
        this.$http = $http;
    }

    generate(){
        console.log("Hello");
        this.$http.post('/validate', this.decl106)
            .then((response)=>{

                this.response = response.data;

                if(this.response['fileId'] != '') {
                    this.pdf = this.response['fileId']
                }

                console.log(this.response);
            })
    }
}

angular.module('declaratii', [])
    .controller('mainController', TsController);