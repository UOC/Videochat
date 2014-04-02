/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


 function StreamObject(userkey, username, publishName) {
    this.userkey = userkey;
    this.username = username;
    this.publishName = publishName;
}
function returnPositionUser(userkey, array){
    var pos = -1;
    for(i=0; i<array.length; i++) {
        if (array[i].userkey===userkey) {
            pos = i;
            break;
        }
    }
    return pos;
}