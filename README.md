# ProgressBarView

Custom Progress Bar View for android, inspired by iOS's UISlider.

## Getting Started - Setup

Add the following to your repositories.

```
    maven {
        url  "http://dl.bintray.com/g20ready/ProgressBarView"
    }
```

Add the following to your dependencies.

```
    compile 'com.max.progressbarview:progressbarview:1.0.0'
```

## Usage

You can either define the library in your xml resources

```xml

    <com.max.progressbarview.ProgressBarView
            android:id="@+id/progressView"
            android:layout_width="wrap_content"
            android:layout_height="50dp"/>

```

or instantiate it via code

```
    RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

    ProgressBarView progressBarView = new ProgressBarView(this);
    progressBarView.setLayoutParams(progressBarParams); 
```