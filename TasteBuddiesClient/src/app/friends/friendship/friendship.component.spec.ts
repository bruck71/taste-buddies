import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendshipComponent } from './friendship.component';

describe('FriendshipComponent', () => {
  let component: FriendshipComponent;
  let fixture: ComponentFixture<FriendshipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriendshipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FriendshipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
