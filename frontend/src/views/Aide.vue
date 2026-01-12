<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('help.title') }}</h1>
    </template>

    <div class="page-surface">
      <div class="help-container">
        <div class="help-hero">
          <div class="hero-icon">?</div>
          <h2>{{ $t('help.subtitle') }}</h2>
          <p>{{ $t('help.description') }}</p>
        </div>

        <div class="help-grid">
          <div class="help-card">
            <h3>{{ $t('help.faq') }}</h3>
            <ul class="faq-list">
              <li>
                <details>
                  <summary>{{ $t('help.q1') }}</summary>
                  <p>{{ $t('help.a1') }}</p>
                </details>
              </li>
              <li>
                <details>
                  <summary>{{ $t('help.q2') }}</summary>
                  <p>{{ $t('help.a2') }}</p>
                </details>
              </li>
              <li>
                <details>
                  <summary>{{ $t('help.q3') }}</summary>
                  <p>{{ $t('help.a3') }}</p>
                </details>
              </li>
            </ul>
          </div>

          <div class="help-card">
            <h3>{{ $t('help.contact') }}</h3>
            <div class="contact-item">
              <span class="icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M4 6a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6z" />
                  <path d="M4 7l8 5 8-5" />
                </svg>
              </span>
              <div>
                <strong>{{ $t('help.email') }}</strong>
                <br>
                <a href="mailto:support@gestiut.fr">support@gestiut.fr</a>
              </div>
            </div>
            <div class="contact-item">
              <span class="icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5 4h4l2 5-3 2a11 11 0 0 0 5 5l2-3 5 2v4a2 2 0 0 1-2 2A16 16 0 0 1 3 6a2 2 0 0 1 2-2z" />
                </svg>
              </span>
              <div>
                <strong>{{ $t('help.phone') }}</strong>
                <br>
                <a href="tel:+33555434343">05 55 43 43 43</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'AideView',
  components: { DashboardLayout }
}
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.4rem;
  font-weight: 700;
  color: var(--color-text-header);
  margin: 0;
}

.page-surface {
  background: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: var(--shadow);
  min-height: 60vh;
}

.help-container {
  max-width: 900px;
  margin: 0 auto;
}

.help-hero {
  text-align: center;
  margin-bottom: 3rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid var(--color-border);
}

.hero-icon {
  width: 60px;
  height: 60px;
  background-color: var(--color-primary);
  color: white;
  font-size: 2rem;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  box-shadow: 0 4px 10px rgba(var(--color-primary-rgb), 0.3);
}

.help-hero h2 { margin: 0.5rem 0; color: var(--color-text-header); }
.help-hero p { color: var(--color-text-body); font-size: 1.1rem; }

.help-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.help-card {
  background: var(--color-sidebar-bg);
  padding: 1.5rem;
  border-radius: 15px;
  border: 1px solid var(--color-border);
}

.help-card h3 { margin-top: 0; color: var(--color-primary); border-bottom: 2px solid var(--color-border); padding-bottom: 0.5rem; margin-bottom: 1rem; }

.faq-list { list-style: none; padding: 0; }
.faq-list li { margin-bottom: 1rem; }

details summary { cursor: pointer; font-weight: 600; color: var(--color-text-header); padding: 0.5rem 0; }
details p { margin: 0.5rem 0 0; color: var(--color-text-body); padding-left: 1rem; border-left: 2px solid var(--color-border); }

.contact-item { display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem; background: var(--color-card-bg); padding: 1rem; border-radius: 10px; border: 1px solid var(--color-border); }
.contact-item .icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: rgba(var(--color-primary-rgb), 0.1);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--color-primary);
}
.contact-item .icon svg {
  width: 18px;
  height: 18px;
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}
.contact-item a { color: var(--color-primary); text-decoration: none; font-weight: 600; }
.contact-item a:hover { text-decoration: underline; }
</style>