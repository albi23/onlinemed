export enum AlertType {
  SUCCESS,
  INFO,
  WARNING,
  DANGER,
}

export class Alert {
  type: AlertType;
  message: any;
  constructor(type: AlertType, message: any) {
    this.type = type;
    this.message = message;
  }

  getClassOfAlert(): string {
    switch (+this.type) {
      case AlertType.DANGER:
        return 'alert alert-danger';
      case AlertType.WARNING:
        return 'alert alert-warning';
      case AlertType.SUCCESS:
        return 'alert alert-success';
      case AlertType.INFO:
        return 'alert alert-info';
      default:
        return 'alert alert-info';
    }
  }
}
