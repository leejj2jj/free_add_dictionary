const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
  deleteButton.addEventListener('click', event => {
    let id = document.getElementById('id').value;
    fetch(`/api/words/${id}`, {
      method: 'DELETE',
      headers: {
        [csrfHeader]: csrfToken
      }
    })
      .then(() => {
        alert('삭제가 완료되었습니다.');
        location.replace('/words');
      });
  });
}

const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
  modifyButton.addEventListener('click', event => {
    let id = document.getElementById('id').value;

    fetch(`/api/words/${id}`, {
      method: 'PUT',
      headers: {
        "Content-Type": "application/json",
        [csrfHeader]: csrfToken
      },
      body: JSON.stringify({
        name: document.getElementById('name').value,
        language: document.getElementById('language').value,
        partOfSpeech: document.getElementById('partOfSpeech').value,
        pronunciation: document.getElementById('pronunciation').value,
        meaning: document.getElementById('meaning').value
      })
    })
      .then(() => {
        alert('수정이 완료되었습니다.');
        location.replace(`/words/${id}`);
      });
  });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
  createButton.addEventListener('click', event => {
    fetch('/api/words', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
        [csrfHeader]: csrfToken
      },
      body: JSON.stringify({
        name: document.getElementById('name').value,
        language: document.getElementById('language').value,
        partOfSpeech: document.getElementById('partOfSpeech').value,
        pronunciation: document.getElementById('pronunciation').value,
        meaning: document.getElementById('meaning').value
      }),
    }).then(() => {
      alert('등록이 완료되었습니다.');
      location.replace('/words');
    });
  });
}