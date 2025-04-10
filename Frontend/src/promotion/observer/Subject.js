// Frontend/src/promotion/observer/Subject.js
export class EventService {
  constructor() {
    this.observers = [];
  }

  addObserver(observer) {
    this.observers.push(observer);
  }

  notifyObservers(eventData) {
    this.observers.forEach((observer) => observer.update(eventData));
  }

  promoteEvent(eventData) {
    console.log("Promoting Event:", eventData.name);
    this.notifyObservers(eventData);
  }
}
