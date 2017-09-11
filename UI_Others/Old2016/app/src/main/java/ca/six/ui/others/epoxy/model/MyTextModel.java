package ca.six.ui.others.epoxy.model;

import com.airbnb.epoxy.EpoxyModelWithHolder;

import ca.six.ui.others.R;


public class MyTextModel extends EpoxyModelWithHolder<MyTextViewHolder> {

    private String leftText;

    private String rightText;

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_two_tv;
    }

    @Override
    protected MyTextViewHolder createNewHolder() {
        return new MyTextViewHolder();
    }

    @Override
    public void bind(MyTextViewHolder holder) {
        super.bind(holder);
        holder.tvLeft.setText(leftText);
        holder.tvRight.setText(rightText);
    }


    // ============== Mandatory Part ==============

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MyTextModel that = (MyTextModel) o;

        if (leftText != null ? !leftText.equals(that.leftText) : that.leftText != null)
            return false;
        return rightText != null ? rightText.equals(that.rightText) : that.rightText == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (leftText != null ? leftText.hashCode() : 0);
        result = 31 * result + (rightText != null ? rightText.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MyTextModel{" +
                "leftText='" + leftText + '\'' +
                ", rightText='" + rightText + '\'' +
                '}' + super.toString();
    }
}
