document.addEventListener("keydown", function(event) {
	if (event.code === "KeyE") {

		if ($('#EmptyCB').prop("checked") == true) {

			$('#EmptyCB').prop('checked', false);
		}
		else {
			$('#EmptyCB').prop('checked', true);

		}

		event.preventDefault();
	}

	if (event.code === "KeyM") {
			if ($('#MultiAB').prop("checked") == true) {

			$('#MultiAB').prop('checked', false);
		}
		else {
			$('#MultiAB').prop('checked', true);

		}

		//$('#MultiAB').prop('checked', !$('#MultiAB').checked);

		event.preventDefault();
	}

	if (event.code === "KeyC") {
			if ($('#ComplexME').prop("checked") == true) {

			$('#ComplexME').prop('checked', false);
		}
		else {
			$('#ComplexME').prop('checked', true);

		}

		//$('#ComplexME').prop('checked', !$('#ComplexME').checked);

		event.preventDefault();
	}

	if (event.code === "KeyL") {
			if ($('#LongPM').prop("checked") == true) {

			$('#LongPM').prop('checked', false);
		}
		else {
			$('#LongPM').prop('checked', true);

		}

		//$('#LongPM').prop('checked', !$('#LongPM').checked);

		event.preventDefault();
	}

//	if (event.code === "KeyI") {
//
//		$('#InsuffMod').prop('checked', !$('#InsuffMod').checked);
//
//		event.preventDefault();
//	}

});