<template>
  <transition name="modal-fade">
    <div class="modal-overlay" @click="$emit('close')">
      <div class="modal-card" @click.stop :class="type">
        <div class="modal-icon">
          <div v-if="type === 'success'" class="icon-circle success">
            <svg viewBox="0 0 24 24" width="32" height="32" stroke="currentColor" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>
          </div>
          <div v-else-if="type === 'error'" class="icon-circle error">
            <svg viewBox="0 0 24 24" width="32" height="32" stroke="currentColor" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
          </div>
          <div v-else class="icon-circle warning">
            <svg viewBox="0 0 24 24" width="32" height="32" stroke="currentColor" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
          </div>
        </div>
        
        <h3 class="modal-title">{{ title }}</h3>
        <p class="modal-message">{{ message }}</p>
        
        <div class="modal-actions">
          <button v-if="showCancel" class="btn-modal btn-cancel" @click="$emit('close')">
            {{ cancelLabel || 'Annuler' }}
          </button>
          <button class="btn-modal btn-confirm" @click="$emit('confirm')">
            {{ confirmLabel || 'OK' }}
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'CustomModal',
  props: {
    title: { type: String, required: true },
    message: { type: String, default: '' },
    type: { type: String, default: 'info' }, // success, error, warning, info
    showCancel: { type: Boolean, default: false },
    confirmLabel: { type: String, default: '' },
    cancelLabel: { type: String, default: '' }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-card {
  background: var(--color-card-bg, #fff);
  padding: 30px;
  border-radius: 24px;
  width: 90%;
  max-width: 420px;
  text-align: center;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  border: 1px solid var(--color-border, #eee);
  transform: translateY(0);
  transition: all 0.3s ease;
}

.modal-icon {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.icon-circle {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.icon-circle.success { background: #10b981; box-shadow: 0 10px 20px rgba(16, 185, 129, 0.3); }
.icon-circle.error { background: #ef4444; box-shadow: 0 10px 20px rgba(239, 68, 68, 0.3); }
.icon-circle.warning { background: #f59e0b; box-shadow: 0 10px 20px rgba(245, 158, 11, 0.3); }

.modal-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0 0 10px;
}

.modal-message {
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  color: var(--color-text-body, #555);
  margin-bottom: 24px;
  line-height: 1.5;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.btn-modal {
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  border: none;
  transition: transform 0.2s, box-shadow 0.2s;
}

.btn-confirm { background: var(--color-primary, #c00000); color: white; flex: 1; }
.btn-confirm:hover { background: var(--color-primary-dark, #a00000); transform: translateY(-2px); box-shadow: 0 4px 12px rgba(192, 0, 0, 0.2); }

.btn-cancel { background: transparent; border: 1px solid var(--color-border, #ddd); color: var(--color-text-body, #555); }
.btn-cancel:hover { background: var(--color-sidebar-bg, #f5f5f5); }

/* Animations */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }
.modal-fade-enter-active .modal-card { animation: popIn 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275); }
@keyframes popIn { from { transform: scale(0.9) translateY(20px); opacity: 0; } to { transform: scale(1) translateY(0); opacity: 1; } }
</style>