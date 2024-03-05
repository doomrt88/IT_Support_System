/**
 * This is intended for the dialog. An attempt to make it generic
 */

 function openGenericModal(title, contentPath, onSave) {
    // Set the modal title
    $('#modalTitle').text(title);
    
    // Load content from the specified path
    $('#modalBody').load(contentPath, function(response, status, xhr) {
        if (status == "error") {
            $('#modalBody').html("<p>Error loading content.</p>");
        }
    });
    
    $('#modalSaveButton').off('click').on('click', function() {
        onSave(function(success) {
			console.log(success);
            if (success) {
                $('#genericModal').modal('hide');
            } else {
                console.log('Save operation unsuccessful.');
            }
        });
    });

    $('#genericModal').modal('show');
}
