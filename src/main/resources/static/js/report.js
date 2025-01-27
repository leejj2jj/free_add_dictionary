document.addEventListener('DOMContentLoaded', () => {
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

  const deleteButton = document.getElementById('delete-btn');
  if (deleteButton) {
    deleteButton.addEventListener('click', event => {
      let id = document.getElementById('id').value;
      fetch(`/api/reports/${id}`, {
        method: 'DELETE',
        headers: {
          [csrfHeader]: csrfToken
        }
      })
        .then(() => {
          alert('삭제가 완료되었습니다.');
          location.replace('/reports');
        });
    });
  }

  const modifyButton = document.getElementById('modify-btn');
  if (modifyButton) {
    modifyButton.addEventListener('click', event => {
      let params = new URLSearchParams(location.search);
      let id = params.get('id');

      fetch(`/api/reports/${id}`, {
        method: 'PUT',
        headers: {
          "Content-Type": "application/json",
          [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
          title: document.getElementById('title').value,
          content: document.getElementById('content').value
        })
      })
        .then(() => {
          alert('수정이 완료되었습니다.');
          location.replace(`/reports/${id}`);
        });
    });
  }

  const createButton = document.getElementById('create-btn');
  if (createButton) {
    createButton.addEventListener('click', event => {
      fetch('/api/reports', {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
          [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
          title: document.getElementById('title').value,
          content: document.getElementById('content').value
        }),
      }).then(() => {
        alert('등록이 완료되었습니다.');
        location.replace('/reports');
      });
    });
  }
});