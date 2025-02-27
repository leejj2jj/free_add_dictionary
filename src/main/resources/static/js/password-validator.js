document.addEventListener('DOMContentLoaded', function () {
  const passwordInput = document.getElementById('password');
  if (passwordInput) {
    passwordInput.addEventListener('input', validatePassword);
  }
});

function validatePassword() {
  const password = document.getElementById('password').value;
  const strengthIndicator = document.getElementById('password-strength');
  const submitButton = document.querySelector('button[type="submit"]');

  const conditions = {
    length: password.length >= 8,
    uppercase: /[A-Z]/.test(password),
    lowercase: /[a-z]/.test(password),
    number: /[0-9]/.test(password),
    special: /[!@#$%^&*]/.test(password)
  };

  let strength = 0;
  Object.values(conditions).forEach(condition => {
    if (condition) strength++;
  });

  let color = '';
  let message = '';

  switch (strength) {
    case 0:
    case 1:
      color = '#dc3545'; // 빨간색
      message = '매우 약함';
      break;
    case 2:
      color = '#ffc107'; // 노란색
      message = '약함';
      break;
    case 3:
      color = '#17a2b8'; // 파란색
      message = '보통';
      break;
    case 4:
      color = '#28a745'; // 초록색
      message = '강함';
      break;
    case 5:
      color = '#20c997'; // 민트색
      message = '매우 강함';
      break;
  }

  strengthIndicator.style.color = color;
  strengthIndicator.textContent = `비밀번호 강도: ${message}`;

  submitButton.disabled = strength < 3;

  return strength >= 3;
}