const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
  deleteButton.addEventListener('click', event => {
    let id = document.getElementById('id').value;
    fetch(`/api/words/${id}`, {
      method: 'DELETE'
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
    let params = new URLSearchParams(location.search);
    let id = params.get('id');

    fetch(`/api/words/${id}`, {
      method: 'PUT',
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: document.getElementById('name').value,
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
      },
      body: JSON.stringify({
        name: document.getElementById('name').value,
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