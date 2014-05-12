
var ImportDialog = {
	init : function(ed) {
	    tinyMCEPopup.execCommand('mceInsertContent', false, '<P> Test </P>');
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(ImportDialog.init, ImportDialog);
window.alert("test");