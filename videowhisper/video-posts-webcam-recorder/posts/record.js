
var RecordDialog = {
	init : function(ed) {
	    tinyMCEPopup.execCommand('mceInsertContent', false, '<P> Test </P>');
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(RecordDialog.init, RecordDialog);
window.alert("test");