function showDeleteConfirmation(userId, userEmail, type) {
  const modalHTML = `
      <div class="modal fade" id="deleteConfirmModal" tabindex="-1">
          <div class="modal-dialog">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title">삭제 확인</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                      <p class="text-danger">삭제하면 복구할 수 없습니다.</p>
                      <p>삭제를 확인하려면 아래에 <strong>${userEmail}</strong>을 입력하세요.</p>
                      <input type="text" id="confirmEmail" class="form-control">
                  </div>
                  <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                      <button type="button" class="btn btn-danger" id="confirmDeleteBtn" disabled>삭제</button>
                  </div>
              </div>
          </div>
      </div>
  `;

  const existingModal = document.getElementById('deleteConfirmModal');
  if (existingModal) {
    existingModal.remove();
  }
  document.body.insertAdjacentHTML('beforeend', modalHTML);

  const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
  const confirmInput = document.getElementById('confirmEmail');
  const confirmButton = document.getElementById('confirmDeleteBtn');

  confirmInput.addEventListener('input', () => {
    confirmButton.disabled = confirmInput.value !== userEmail;
  });

  confirmButton.addEventListener('click', () => {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = type === 'admin' ? `/api/admin/users/${userId}` : `/api/user/${userId}`;

    const methodInput = document.createElement('input');
    methodInput.type = 'hidden';
    methodInput.name = '_method';
    methodInput.value = 'DELETE';

    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = '_csrf';
    csrfInput.value = document.querySelector('meta[name="_csrf"]').content;

    form.appendChild(methodInput);
    form.appendChild(csrfInput);
    document.body.appendChild(form);
    form.submit();
  });

  modal.show();
}