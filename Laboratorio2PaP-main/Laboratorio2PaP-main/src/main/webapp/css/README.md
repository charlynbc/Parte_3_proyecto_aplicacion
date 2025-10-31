# CSS Architecture for Turismo.uy

This document describes the modular CSS architecture implemented for the Turismo.uy web application.

## File Structure

```
css/
├── main.css                 # Main entry point (use for new pages)
├── styles.css              # Legacy compatibility file (imports all modules)
├── base.css                # Reset, typography, utility classes
├── layout.css              # Container layouts, page structure
├── navbar.css              # Navigation bar component
├── footer.css              # Footer component
├── forms.css               # Form inputs, fieldsets, validation
├── buttons.css             # Button styles and variants
├── cards.css               # Card components and grids
├── messages.css            # Alert and message components
├── registration.css        # DEPRECATED - kept for compatibility
└── form-improvements.css   # DEPRECATED - kept for compatibility
```

## Usage

### For New Pages

Include only the main stylesheet:

```html
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
```

### Module Descriptions

1. **base.css** - Foundation styles
   - CSS reset
   - Typography (h1-h6, p, a)
   - Utility classes (margins, padding, text alignment)

2. **layout.css** - Page structure
   - Main containers (.container, .main-content)
   - Hero sections (.hero-section, .registration-hero)
   - Page headers (.page-header)
   - Responsive grid layouts

3. **navbar.css** - Navigation component
   - Fixed navbar (.navbar)
   - Navigation menu (.nav-menu)
   - User section (.nav-user)

4. **footer.css** - Footer component
   - Footer styles
   - Footer links

5. **forms.css** - Form components
   - Form groups (.form-group)
   - Input fields (input, select, textarea)
   - Password toggle (.password-input)
   - File inputs (.file-input)
   - Form validation states
   - Fieldsets and legends

6. **buttons.css** - Button components
   - Primary buttons (.btn, .btn-primary)
   - Secondary buttons (.btn-secondary)
   - Small buttons (.btn-small)
   - Register buttons (.register-btn)
   - Link buttons (.back-link, .help-link)

7. **cards.css** - Card components
   - Card layouts (.card, .card-grid)
   - Navigation cards (.nav-card)
   - Info sections (.info-section)
   - Features list (.features-list)
   - Empty states (.empty-state)

8. **messages.css** - Message components
   - Error messages (.error-message)
   - Success messages (.success-message)
   - Info messages (.info-message)
   - Warning messages (.warning-message)

## Best Practices

1. **No inline styles** - Always use CSS classes from the modules
2. **Responsive first** - All components are mobile-friendly
3. **Consistent spacing** - Use utility classes (.mt-1, .mb-2, etc.)
4. **Semantic HTML** - Use appropriate HTML5 elements
5. **Accessibility** - All interactive elements have proper focus states

## Migration Notes

### Old Approach (Deprecated)
```html
<style>
  .my-custom-style { ... }
</style>
```

### New Approach
```html
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<!-- Use existing classes or add to appropriate module -->
```

## Common Patterns

### Form with Hero Section
```html
<div class="registration-container">
    <div class="registration-hero">
        <div class="hero-icon">🎯</div>
        <h1>Title</h1>
        <p>Description</p>
    </div>
    <div class="registration-form-section">
        <form class="registration-form">
            <!-- form content -->
        </form>
    </div>
</div>
```

### Card Grid
```html
<div class="card-grid">
    <div class="card">
        <div class="card-image">...</div>
        <div class="card-content">
            <div class="card-title">Title</div>
            <div class="card-description">Description</div>
            <div class="card-actions">
                <a href="#" class="btn-small">Action</a>
            </div>
        </div>
    </div>
</div>
```

### Page with Navbar
```html
<jsp:include page="/WEB-INF/jsp/includes/navbar.jsp"/>
<div class="main-content">
    <div class="page-header">
        <h1>Page Title</h1>
        <p>Page description</p>
    </div>
    <!-- page content -->
</div>
```

## Maintenance

- Keep each module focused on its specific component
- Add new styles to the appropriate module
- Update this README when adding new patterns
- Test all changes on mobile, tablet, and desktop viewports

## Browser Support

- Modern browsers (Chrome, Firefox, Safari, Edge)
- CSS Grid and Flexbox required
- No IE11 support

---
Last updated: October 2025
