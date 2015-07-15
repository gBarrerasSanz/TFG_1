<script>
var CatalogClient = function() 
{
	var constructor = function CatalogClient()
	{
		//
	}

	var getContentFromUrl = function(url)
	{
		var result = []
		$.getJSON(url, function (jsonContent) {
			return jsonContent;
		});
	}


}
</script>