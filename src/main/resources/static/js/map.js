const WORLD_BOUNDS = L.latLngBounds(
    [-85, -180],
    [85, 180]
);

// Crea la mappa centrata su Milano con uno zoom iniziale cittadino.
export const map = L.map('map', {
    center: [45.4642, 9.1900],
    zoom: 12,
    maxZoom: 18,
    minZoom: 6,
    maxBounds: WORLD_BOUNDS,
    maxBoundsViscosity: 1,
    worldCopyJump: false,
    dragging: false,
    scrollWheelZoom: false
});

// Usa una base Carto chiara, leggibile sotto marker e popup.
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; OpenStreetMap & CartoDB',
    noWrap: true,
    bounds: WORLD_BOUNDS
}).addTo(map);

function setMouseMapInteraction(isEnabled) {
    if (isEnabled) {
        map.dragging.enable();
        map.scrollWheelZoom.enable();
        return;
    }

    map.dragging.disable();
    map.scrollWheelZoom.disable();
}

document.addEventListener('keydown', event => {
    if (event.key === 'Control') setMouseMapInteraction(true);
});

document.addEventListener('keyup', event => {
    if (event.key === 'Control') setMouseMapInteraction(false);
});

window.addEventListener('blur', () => setMouseMapInteraction(false));
