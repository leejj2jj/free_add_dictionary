document.addEventListener('DOMContentLoaded', function () {
  // Bootstrap tooltip initialization
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)
  });

  // Form validation
  var forms = document.querySelectorAll('.needs-validation')
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener('submit', function (event) {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }
      form.classList.add('was-validated')
    }, false)
  });

  // Alert auto-close
  var alerts = document.querySelectorAll('.alert-dismissible')
  alerts.forEach(function (alert) {
    setTimeout(function () {
      var dismissButton = alert.querySelector('.btn-close')
      if (dismissButton) {
        dismissButton.click()
      }
    }, 3000)
  });
});
