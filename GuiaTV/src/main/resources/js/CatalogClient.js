var CatalogClient = {
	getContentFromUrl : function(url)
	{
		var result = []
		$.getJSON(url, function (jsonContent) {
			return jsonContent;
		});
	}
}